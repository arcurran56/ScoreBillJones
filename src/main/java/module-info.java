module ScoreBillJones {
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.poi.poi;
    requires xstream;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    //requires javac2;
    //requires javac2;
    opens scorebj.model to xstream;

}