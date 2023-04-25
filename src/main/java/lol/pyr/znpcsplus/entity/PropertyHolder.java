package lol.pyr.znpcsplus.entity;

public interface PropertyHolder {
    <T> T getProperty(EntityProperty<T> key);
    boolean hasProperty(EntityProperty<?> key);

    PropertyHolder EMPTY = new PropertyHolder() {
        @Override
        public <T> T getProperty(EntityProperty<T> key) {
            return null;
        }

        @Override
        public boolean hasProperty(EntityProperty<?> key) {
            return false;
        }
    };
}
