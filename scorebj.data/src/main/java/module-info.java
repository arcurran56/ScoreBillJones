module scorebj.data {
    exports scorebj.model;
    requires xstream;
    requires org.apache.logging.log4j;
    opens scorebj.model to xstream;
}