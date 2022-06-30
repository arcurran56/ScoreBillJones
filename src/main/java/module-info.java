module ScoreBillJones {
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.poi.poi;
    requires xstream;
    requires java.desktop;
    //requires javac2;
    requires org.apache.poi.ooxml;
    opens scorebj.model to xstream;

}