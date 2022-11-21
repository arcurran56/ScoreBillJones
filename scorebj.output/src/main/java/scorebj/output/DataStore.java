package scorebj.output;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.Competition;
import scorebj.model.DataStoreException;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Singleton class representing persistent data for the application.  Version for testing.
 */
public class DataStore {

    private static final String APP_FOLDER_NAME = "scorebj";
    private static final String TEST_APP_FOLDER_NAME = "scorebj-test";
    private static final String DATA_FOLDER_NAME = "data";

    protected static final Logger logger = LogManager.getLogger();

    public final static int TEST=1;
    public final static int BLANK=2;

    private static File persistenceLocation;
    protected static PersistenceStrategy persistenceStrategy;
    protected final static XStream xStream = new XStream();

    protected static DataStore dataStore;

    private final Map<String, Competition> competitions = new Hashtable<>();
    protected static Map<String, Competition> persistentCompetitions;
    private static boolean testMode = false;
    private static Competition testCompetition;


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

        URL testFolderURL = ClassLoader.getSystemClassLoader().getResource("test-data");
        persistenceLocation = new File(testFolderURL.getFile());
        assert persistenceLocation.exists();

        logger.debug("...dataStore in " + persistenceLocation);

        persistenceStrategy =
                new FilePersistenceStrategy(persistenceLocation, xStream);
        persistentCompetitions = (Map<String,Competition>) Collections.synchronizedMap((Map<String,Competition>) new XmlMap(persistenceStrategy));

        logger.debug( "...exiting initialise");
    }

    public static void destroy(){
        dataStore = null;
    }

    public DataStore()  {
    }

    /**
     * Return competition for given competitionId from persistent store.  Updates competitions hash with this
     * competition because competition returned has different reference from when it was saved.
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

}

