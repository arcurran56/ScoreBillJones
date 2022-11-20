module scorebj.data {
    exports scorebj.model;
    requires xstream;
    requires org.apache.logging.log4j;
    requires java.desktop;
    opens scorebj.model to xstream;
}