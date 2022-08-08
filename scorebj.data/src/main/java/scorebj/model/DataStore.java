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

import java.util.*;

    /**
     * Singleton class representing persistent data for the application.
     */
    public class DataStore {

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

        /**
         * Create a new Datastore if it does not already exist and return it.  Alternatively return the existing one.
         * @return the Datastore singleton.
         */
        public static DataStore create() throws DataStoreException {
            return create(false);
        }
        public static DataStore create(boolean test) throws DataStoreException {
            logger.debug("...creating...");
            testMode = test;
                try {
                    if (dataStore == null) {
                        logger.debug("...new datastore creation.");
                        dataStore = new DataStore();
                        if (!test) {
                            dataStore.initialise();
                        }                    }
                } catch (Exception e) {
                    logger.error(e.getStackTrace());
                    throw new DataStoreException();
                }


            if (testMode){
                logger.debug("...test mode...");
                xStream.addPermission(AnyTypePermission.ANY);
                //xStream.allowTypes(ALLOWED_TYPES);

                File dataLocation = null;
                Reader xmlReader;
                try {
                    dataLocation = new File(ClassLoader.getSystemResource("test_data.xml").toURI());
                    xmlReader = new FileReader(dataLocation);
                    testCompetition = (Competition) xStream.fromXML(xmlReader);

                } catch (URISyntaxException | FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                logger.debug("...reading from " + dataLocation.getAbsolutePath());

            }
            return dataStore;
        }

        /*
        Initialise the Datastore with a single competition with test data.  Data is persisted in a series of XML files.
         */
        protected void initialise() throws IOException {
            logger.debug("...initialising...");

            xStream.autodetectAnnotations(true);
            xStream.addPermission(AnyTypePermission.ANY);
            //xStream.allowTypes(ALLOWED_TYPES);

            persistenceLocation = new File(System.getProperty("user.home"), "scorebj");
            File dataLocation = new File(persistenceLocation,"data");
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
            }
            else {
                competition = testCompetition;
            }
            return competition;
        }

        public Competition getCompetition(String name) {

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
            persistentCompetitions.remove(key);
        }

        public static File getPersistenceLocation() {
            return persistenceLocation;
        }

        /**
         * Get list of saved Competitions.
         */
        public Set<String> getCompetitionNames(){
            return persistentCompetitions.keySet();
        }
    }
