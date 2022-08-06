module scorebj.output {
    requires scorebj.data;
    requires org.apache.logging.log4j;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires xstream;
    opens scorebj.output to xstream;
    exports scorebj.output;
}