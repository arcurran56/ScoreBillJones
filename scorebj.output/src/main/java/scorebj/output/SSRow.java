package scorebj.output;

import java.util.ArrayList;
import java.util.List;

public class SSRow implements Comparable<SSRow> {
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    private String pair;
    private final List<String> setResult = new ArrayList<>(10);
    private int total;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public List<String> getSetResult() {
        return setResult;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int compareTo(SSRow ssRow) {
        int result = Integer.compare(ssRow.total, this.total);
        return result;
    }

}
