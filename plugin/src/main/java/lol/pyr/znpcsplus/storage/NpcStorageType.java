package lol.pyr.znpcsplus.storage;

import lol.pyr.znpcsplus.storage.yaml.YamlStorage;

public enum NpcStorageType {
    YAML {
        @Override
        public NpcStorage create() {
            return new YamlStorage();
        }
    };

    public abstract NpcStorage create();
}
