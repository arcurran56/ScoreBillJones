module scorebj.data {
    requires xstream;
    requires org.apache.logging.log4j;
    requires java.desktop;
    exports scorebj.model;
    opens scorebj.model to xstream;
}