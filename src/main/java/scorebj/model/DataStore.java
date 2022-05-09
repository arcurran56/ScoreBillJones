package scorebj.model;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlArrayList;
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

        private static final Logger logger = LogManager.getLogger("scorebj.model.DataStore");

        public final static int TEST=1;
        public final static int BLANK=2;

        private static File persistenceLocation;
        private static PersistenceStrategy persistenceStrategy;
        private final static XStream xStream = new XStream();

        protected static DataStore dataStore;

        private final Map<Integer,Competition> competitions = new Hashtable<>();
        private static List<Competition> persistentCompetitions;

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
            logger.debug("...writing to " + persistenceLocation.getAbsolutePath());
            if (!persistenceLocation.exists() && !persistenceLocation.mkdir())
                throw new IOException(String.format("Can't create persistence directory, %s", persistenceLocation.getAbsolutePath()));
            persistenceStrategy =
                    new FilePersistenceStrategy(persistenceLocation, xStream);
            persistentCompetitions = Collections.synchronizedList(new XmlArrayList(persistenceStrategy));

            Competition competition;
            if(persistentCompetitions.isEmpty()) {
                competition = new Competition();
                persistentCompetitions.add(competition);
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
            Competition competition = persistentCompetitions.get(competitionId);
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

            if (!persistentCompetitions.isEmpty()) persistentCompetitions.remove(0);
            persistentCompetitions.add(0,competition);

            logger.debug("...exit persist.");
        }

        public static File getPersistenceLocation() {
            return persistenceLocation;
        }
    }
