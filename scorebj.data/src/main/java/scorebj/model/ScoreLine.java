package scorebj.model;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ScoreLine implements PropertyChangeListener, TableModelListener {
    @XStreamOmitField
    static Logger logger = LogManager.getLogger();
    @XStreamOmitField
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Property change event for, " + evt.getPropertyName() + " from " + evt.getSource().toString());
        if (!"score".equals(evt.getPropertyName())) {
            if (entry[Columns.CONTRACT.ordinal()] != null
                    && entry[Columns.PLAYED_BY.ordinal()] != null
                    && entry[Columns.TRICKS.ordinal()] != null) {
                scoreHand();
                pcs.firePropertyChange("score", 0, 1);
            }
        }
    }

    private BoardId.Vulnerability vulnerability = BoardId.Vulnerability.NONE;


    public void setVulnerability(BoardId.Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        logger.debug("Table change event received from " + e.getSource().toString());
    }

    enum Columns {NS_PAIR, EW_PAIR, CONTRACT, PLAYED_BY, TRICKS, NS_SCORE, EW_SCORE, NS_MPS, EW_MPS}

    private final Object[] entry = new Object[9];

    public enum Direction {N, S, E, W}

    ScoreLine() {
        logger.debug("Creating " + this.toString());
        pcs.addPropertyChangeListener(this);
    }

    public Object get(int index) {
        return entry[index];
    }

    public void set(int index, Object value) {
        Object oldVal = entry[index];
        entry[index] = value;
        pcs.firePropertyChange("contract", oldVal, value);
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
        pcs.firePropertyChange("contract", oldVal, contract);
    }

    public Direction getPlayedBy() {
        return (Direction) entry[Columns.PLAYED_BY.ordinal()];
    }

    public void setPlayedBy(Direction playedBy) {
        Direction oldVal = (Direction) entry[Columns.PLAYED_BY.ordinal()];
        entry[Columns.PLAYED_BY.ordinal()] = playedBy;
        pcs.firePropertyChange("playedBy", oldVal, playedBy);
    }

    public Integer getTricks() {
        return (Integer) entry[Columns.TRICKS.ordinal()];
    }

    public void setTricks(Integer tricks) {
        Integer oldVal = (Integer) entry[Columns.TRICKS.ordinal()];
        entry[Columns.TRICKS.ordinal()] = tricks;
        pcs.firePropertyChange("tricks", oldVal, tricks);
    }

    public Integer getNSScore() {
        return (Integer) entry[Columns.NS_SCORE.ordinal()];
    }

    public Integer getEWScore() {
        return (Integer) entry[Columns.EW_SCORE.ordinal()];
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

    private void scoreHand() {
        Integer nsPair = getNsPair();
        Integer ewPair = getEwPair();
        Contract contract = getContract();
        Direction playedBy = getPlayedBy();
        Integer tricks = getTricks();

        Integer NSScore = 0;
        Integer EWScore = 0;
        int score;


        if (nsPair != null
                && ewPair != null
                && contract != null
                && playedBy != null
                && tricks != null) {
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
            }
            entry[Columns.NS_SCORE.ordinal()] = NSScore;
            entry[Columns.EW_SCORE.ordinal()] = EWScore;
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        logger.debug("Adding " + pcl.toString() + " to " + this.toString()  + " as listener.");
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
}
