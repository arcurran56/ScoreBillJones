package scorebj.model;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ScoreLine implements PropertyChangeListener, TableModelListener, Cloneable {
    @XStreamOmitField
    static Logger logger = LogManager.getLogger();
    @XStreamOmitField
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final static int NO_COLUMNS = 11;

    @XStreamOmitField
    private boolean complete = false;

    public boolean isEmpty() {
        boolean empty = true;
        for (Object e : entry) {
            empty = (e == null) && empty;
        }
        return empty;
    }

    private boolean empty = true;

    public void setPcs(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Property change event for, " + evt.getPropertyName() + " from " + evt.getSource().toString());

        String command = evt.getPropertyName();

        switch (command) {
            case "score":
                break;

            case "blank":
                break;

            case "CONTRACT", "PLAYED_BY", "TRICKS":
                    scoreHand();
                break;

            case "recalculate":
                break;

            default: break;
        }


    }

    private BoardId.Vulnerability vulnerability = BoardId.Vulnerability.NONE;


    public void setVulnerability(BoardId.Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        logger.debug("Table change event, " + e.getType() + " received from " + e.getSource().toString());
    }

    public void activate(PropertyChangeListener propertyChangeListener) {
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(this);
        pcs.addPropertyChangeListener(propertyChangeListener);

    }

    public boolean isSkipped() {
        Contract contract = getContract();
        if (contract != null) {
            return getContract().isSkipped();
        } else return false;
    }

    public boolean isPassedOut() {
        Contract contract = getContract();
        if (contract != null) {
            return getContract().isPassedOut();
        } else return false;
    }

    enum Columns {
        NS_PAIR, EW_PAIR, CONTRACT, PLAYED_BY, TRICKS,
        NS_SCORE, EW_SCORE, NS_MPS, EW_MPS,
        NS_OVERRIDE, EW_OVERRIDE
    }

    private final Object[] entry = new Object[NO_COLUMNS];

    public enum Direction {N, S, E, W, PASS}

    public ScoreLine() {
        logger.trace("Creating " + this.toString());
        pcs = new PropertyChangeSupport(this);
    }

    public Object get(int index) {
        return entry[index];
    }

    public void set(int index, Object value) {
        Object oldVal = entry[index];
        entry[index] = value;
        if (value != null) {
            if (index == Columns.CONTRACT.ordinal() && "ALL".equals(value.toString().toUpperCase())) {
                entry[Columns.PLAYED_BY.ordinal()] = Direction.PASS;
                entry[Columns.TRICKS.ordinal()] = 0;
            }
        }
        String propertyName;
        propertyName = Columns.values()[index].toString();
        if (index <= Columns.TRICKS.ordinal()) pcs.firePropertyChange(propertyName, oldVal, value);
    }

    public Integer getNsPair() {
        return (Integer) entry[Columns.NS_PAIR.ordinal()];
    }

    public void setNsPair(Integer nsPair) {
        entry[Columns.NS_PAIR.ordinal()] = nsPair;
    }

    public Integer getEwPair() {
        return (Integer) entry[Columns.EW_PAIR.ordinal()];
    }

    public void setEwPair(Integer ewPair) {
        entry[Columns.EW_PAIR.ordinal()] = ewPair;
    }

    public Contract getContract() {
        return (Contract) entry[Columns.CONTRACT.ordinal()];
    }

    public void setContract(Contract contract) {
        Contract oldVal = (Contract) entry[Columns.CONTRACT.ordinal()];
        entry[Columns.CONTRACT.ordinal()] = contract;
        pcs.firePropertyChange(Columns.CONTRACT.toString(), oldVal, contract);
    }

    public Direction getPlayedBy() {
        return (Direction) entry[Columns.PLAYED_BY.ordinal()];
    }

    public void setPlayedBy(Direction playedBy) {
        Direction oldVal = (Direction) entry[Columns.PLAYED_BY.ordinal()];
        entry[Columns.PLAYED_BY.ordinal()] = playedBy;
        pcs.firePropertyChange(Columns.PLAYED_BY.toString(), oldVal, playedBy);
    }

    public Integer getTricks() {
        return (Integer) entry[Columns.TRICKS.ordinal()];
    }

    public void setTricks(Integer tricks) {
        Integer oldVal = (Integer) entry[Columns.TRICKS.ordinal()];
        entry[Columns.TRICKS.ordinal()] = tricks;
        pcs.firePropertyChange(Columns.TRICKS.toString(), oldVal, tricks);
    }

    public Integer getNSScore() {
        return (Integer) entry[Columns.NS_SCORE.ordinal()];
    }

    public void setNSScore(Integer score) {
        entry[Columns.NS_SCORE.ordinal()] = score;
    }

    public Integer getEWScore() {
        return (Integer) entry[Columns.EW_SCORE.ordinal()];
    }

    public void setEWScore(Integer score) {
        entry[Columns.EW_SCORE.ordinal()] = score;
    }

    public Integer getNsMPs() {
        return (Integer) entry[Columns.NS_MPS.ordinal()];
    }

    public void setNsMPs(Integer nsMPs) {
        entry[Columns.NS_MPS.ordinal()] = nsMPs;
    }

    public Integer getEwMPs() {
        return (Integer) entry[Columns.EW_MPS.ordinal()];
    }

    public void setEwMPs(Integer ewMPs) {
        entry[Columns.EW_MPS.ordinal()] = ewMPs;

    }

    public Integer getNsOverride() {

        return (Integer) entry[Columns.NS_OVERRIDE.ordinal()];
    }

    public void setNsOverride(Integer nsOverride) {

        entry[Columns.NS_OVERRIDE.ordinal()] = nsOverride;
    }

    public Integer getEwOverride() {

        return (Integer) entry[Columns.EW_OVERRIDE.ordinal()];
    }

    public void setEwOverride(Integer ewOverride) {
        entry[Columns.EW_OVERRIDE.ordinal()] = ewOverride;
    }

    /**
     *
     * Calculate score for hand if all necessary fields are completed.
     */
    private void scoreHand() {
        Contract contract = getContract();
        Direction playedBy = getPlayedBy();
        Integer tricks = getTricks();

        Integer NSScore = null;
        Integer EWScore = null;
        int score;

        if (isComplete()) {

            if (isPassedOut() || isSkipped()) {
                if (isPassedOut()) {
                    NSScore = 0;
                    EWScore = 0;
                } else {
                    NSScore = null;
                    EWScore = null;
                }
            } else {
                     switch (playedBy) {
                        case N, S:
                            if (vulnerability == BoardId.Vulnerability.NS || vulnerability == BoardId.Vulnerability.ALL) {
                                score = contract.getScore(tricks, true);
                            } else {
                                score = contract.getScore(tricks, false);
                            }

                            if (score > 0) {
                                NSScore = score;
                                EWScore = 0;
                            } else {
                                NSScore = 0;
                                EWScore = -score;
                            }

                            break;

                        case E, W:
                            if (vulnerability == BoardId.Vulnerability.EW || vulnerability == BoardId.Vulnerability.ALL) {
                                score = contract.getScore(tricks, true);
                            } else {
                                score = contract.getScore(tricks, false);
                            }

                            if (score > 0) {
                                NSScore = 0;
                                EWScore = score;
                            } else {
                                NSScore = -score;
                                EWScore = 0;
                            }
                            break;

                        default:
                    }
                }

        }

        entry[Columns.NS_SCORE.ordinal()] = NSScore;
        entry[Columns.EW_SCORE.ordinal()] = EWScore;

        pcs.firePropertyChange("score", 0, 1);

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        logger.debug("Adding " + pcl.toString() + " to " + this.toString() + " as listener.");
        pcs.addPropertyChangeListener(pcl);
    }

    public void recalculate() {
        pcs.firePropertyChange("recalculate", 0, 1);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    public boolean isComplete() {
        boolean complete;
        complete = getNsPair() != null
                && getEwPair() != null
                && getContract() != null
                && ((getTricks() != null && getPlayedBy() != null) || isSkipped() || isPassedOut());
                //&& getNsMPs() != null
                //&& getEwMPs() != null;

        return complete;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ScoreLine: ");
        for (Object o : entry) {
            builder.append(o);
            builder.append("-");
        }
        builder.append(isComplete() ? "complete" : "incomplete");
        builder.append("-");
        builder.append(isPassedOut() ? "passed" : "contracted");
        builder.append("-");
        builder.append(isSkipped() ? "skipped" : "played");

        return builder.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ScoreLine sl = (ScoreLine) super.clone();
        sl.setNsPair(this.getNsPair());
        sl.setEwPair(this.getEwPair());
        sl.setContract(this.getContract());
        sl.setPlayedBy(this.getPlayedBy());
        sl.setTricks(this.getTricks());

        return sl;
    }
    public void clear() {
        setNsPair(null);
        setEwPair(null);
        setContract(null);
        setPlayedBy(null);
        setTricks(null);
    }
}
