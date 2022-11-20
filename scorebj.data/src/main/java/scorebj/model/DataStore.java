package scorebj.model;

import java.io.*;
import java.net.URISyntaxException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

    /**
     * Singleton class representing persistent data for the application.
     */
    public class DataStore {

        private static final String APP_FOLDER_NAME = "scorebj";
        private static final String TEST_APP_FOLDER_NAME = "scorebj-test";
        private static final String DATA_FOLDER_NAME = "data";

        protected static final Logger logger = LogManager.getLogger();
        //private static final Logger logger = LogManager.getLogger("scorebj.model.DataStore");

        public final static int TEST=1;
        public final static int BLANK=2;

        private static File persistenceLocation;
        protected static PersistenceStrategy persistenceStrategy;
        protected final static XStream xStream = new XStream();

        protected static DataStore dataStore;

        private final Map<String,Competition> competitions = new Hashtable<>();
        protected static Map<String, Competition> persistentCompetitions;
        private static boolean testMode = false;
        private static Competition testCompetition;

        private static class CustomFilePersistenceStrategy extends FilePersistenceStrategy{
            private final File baseDirectory;
            public CustomFilePersistenceStrategy(File baseDirectory) {
                super(baseDirectory);
                this.baseDirectory = baseDirectory;
            }

            public CustomFilePersistenceStrategy(File baseDirectory, XStream xstream) {
                super(baseDirectory, xstream);
                this.baseDirectory = baseDirectory;
            }

            public CustomFilePersistenceStrategy(File baseDirectory, XStream xstream, String encoding, String illegalChars) {
                super(baseDirectory, xstream, encoding, illegalChars);
                this.baseDirectory = baseDirectory;
            }

            @Override
            public Object remove(Object key) {
                super.remove(key);
                logger.debug("Deleting file...");
                System.exit(0);
                File dataFile = new File(baseDirectory,getName(key));
                logger.debug("..." + dataFile.getAbsolutePath());
                try {
                    Files.delete(Path.of(dataFile.getAbsolutePath()));
                    assert !dataFile.exists();
                    System.exit(0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Object obj = super.remove(key);
                return obj;
            }
        }
        /**
         * Create a new Datastore if it does not already exist and return it.  Alternatively return the existing one.
         * @return the Datastore singleton.
         */
        public static DataStore create() throws DataStoreException {

            logger.debug("...creating...");
                try {
                    if (dataStore == null) {
                        logger.debug("...new datastore creation.");
                        dataStore = new DataStore();
                        dataStore.initialise();
                    }
                } catch (Exception e) {
                    logger.error(e.getStackTrace());
                    throw new DataStoreException();
                }

            return dataStore;
        }

        public static void setTestMode(boolean testMode) {
            DataStore.testMode = testMode;
        }

        /*
                Initialise the Datastore with a single competition with test data.  Data is persisted in a series of XML files.
                 */
        protected void initialise() throws IOException {
            logger.debug("...initialising...");

            xStream.autodetectAnnotations(true);
            xStream.addPermission(AnyTypePermission.ANY);
            //xStream.allowTypes(ALLOWED_TYPES);

            String appFolder;
            if(testMode){
                appFolder = TEST_APP_FOLDER_NAME;
            }
            else {
                appFolder = APP_FOLDER_NAME;
            }
            persistenceLocation = new File(System.getProperty("user.home"), appFolder);
            File dataLocation = new File(persistenceLocation,DATA_FOLDER_NAME);
            logger.debug("...writing to " + dataLocation.getAbsolutePath());
            if (!dataLocation.exists() && !dataLocation.mkdir())
                throw new IOException(String.format("Can't create persistence directory, %s", dataLocation.getAbsolutePath()));
            persistenceStrategy =
                    new FilePersistenceStrategy(dataLocation, xStream);
            persistentCompetitions = (Map<String,Competition>) Collections.synchronizedMap((Map<String,Competition>) new XmlMap(persistenceStrategy));

            Competition competition;
            if(persistentCompetitions.isEmpty()) {
                competition = new Competition();
                persistentCompetitions.put(competition.getCompetitionName(), competition);
            }
            logger.debug( "...exiting initialise");
        }

        public static void destroy(){
            dataStore = null;
        }

        public DataStore()  {
        }

        /**
         * Return competition for given competitionId from persistent store.  Updates competitions hash with this competition because competition returned has different reference from when it was saved.
         * @param competitionId the id of the competition to fetch.
         * @return the Competition requested.
         */
        public Competition getCompetition(int competitionId) {
            Competition competition;
            if(!testMode) {
                Set<String> keys = persistentCompetitions.keySet();
                String key = (String) keys.toArray()[0];
                competition = persistentCompetitions.get(key);
                logger.debug("Fetching competition " + competition.getCompetitionName());
            }
            else {
                competition = testCompetition;
                logger.debug("Fetching test competition.");
            }
            return competition;
        }

        public Competition getCompetition(String name) {
            logger.debug("Fetching competition " + name);
            return persistentCompetitions.get(name);
        }

        /**
         * Persist competitions data.  Replace existing entry with updated one.
         */
        public void persist(Competition competition){
            logger.debug("...persisting...");
            StringBuilder logMessage = new StringBuilder()
                    .append(competition.getCompetitionName())
                    .append(" ")
                    .append(competition.getNoPairs())
                    .append(" ")
                    .append(competition.getNoSets())
                    .append(" ")
                    .append(competition.getNoBoardsPerSet());
            logger.debug(logMessage);

            if ( competition.getCompetitionName() != null
                && !"".equals(competition.getCompetitionName())
                && competition.getNoPairs() > 0
                && competition.getNoSets() > 0
                && competition.getNoBoardsPerSet() > 0) {
                persistentCompetitions.put(competition.getCompetitionName(), competition);
            }
            else logger.warn("...ignored.");
            logger.debug("...exit persist.");
        }
        public void delete(String key){
            logger.info("Deleting " + key);
            persistentCompetitions.remove(key);
        }

        public static File getPersistenceLocation() {
            return persistenceLocation;
        }

        /**
         * Get list of saved Competitions.
         */
        public Set<String> getCompetitionNames(){
            Set<String> set = new HashSet<>();
            set.addAll(persistentCompetitions.keySet());
            return set;
        }
    }

