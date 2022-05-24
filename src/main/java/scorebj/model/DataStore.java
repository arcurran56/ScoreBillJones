package scorebj.model;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

    /**
     * Singleton class representing persistent data for the application.
     */
    public class DataStore {

        private static final Logger logger = LogManager.getLogger();
        //private static final Logger logger = LogManager.getLogger("scorebj.model.DataStore");

        public final static int TEST=1;
        public final static int BLANK=2;

        private static File persistenceLocation;
        private static PersistenceStrategy persistenceStrategy;
        private final static XStream xStream = new XStream();

        protected static DataStore dataStore;

        private final Map<String,Competition> competitions = new Hashtable<>();
        private static Map<String, Competition> persistentCompetitions;

        //private static int selectedCompetitionId = TEST;

        /**
         * Create a new Datastore if it does not already exist and return it.  Alternatively return the existing one.
         * @return the Datastore singleton.
         */
        public static DataStore create() {
            logger.debug("...creating...");
            try {
                if (dataStore==null) {
                    logger.debug("...new datastore creation.");
                    dataStore = new DataStore();
                    dataStore.initialise();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            return dataStore;
        }

        /*
        Initialise the Datastore with a single competition with test data.  Data is persisted in a series of XML files.
         */
        private void initialise() throws IOException {
            logger.debug("...initialising...");
            persistenceLocation = new File(System.getProperty("user.home"), "scorebj");
            File dataLocation = new File(persistenceLocation,"data");
            logger.debug("...writing to " + dataLocation.getAbsolutePath());
            if (!dataLocation.exists() && !dataLocation.mkdir())
                throw new IOException(String.format("Can't create persistence directory, %s", dataLocation.getAbsolutePath()));
            persistenceStrategy =
                    new FilePersistenceStrategy(dataLocation, xStream);
            persistentCompetitions = Collections.synchronizedMap(new XmlMap(persistenceStrategy));

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

            Set<String> keys = persistentCompetitions.keySet();
            String key = (String) keys.toArray()[0];
            Competition competition = persistentCompetitions.get(key);
            return competition;
        }

        public Competition getCompetition(String name) {

            Competition competition = persistentCompetitions.get(name);
            return competition;
        }
        public void setCompetition(int competitionId, Competition competition) {
            //persistentCompetitions.(competitionId, competition);
            //persist();
        }

 /*       public Map<Integer,Competition> getCompetitions() {
            logger.debug("...getting competitions...");
            int lastId = 0;
            for (Integer id: persistentCompetitions.keySet()) {
                competitions.put(id, persistentCompetitions.get(id));
                lastId = Math.max(lastId,id);
            }
            Competition.setNextCompetitionId(lastId+1);
            logger.debug("...exit getCompetitions.");
            return competitions;
        }*/

 /*       public void setCompetitions(Map<Integer, Competition> competitions) {
            this.competitions = competitions;
        }*/

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
