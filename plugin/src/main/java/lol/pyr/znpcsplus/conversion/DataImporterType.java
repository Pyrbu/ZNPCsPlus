package lol.pyr.znpcsplus.conversion;

import lol.pyr.znpcsplus.util.LazyLoader;

public enum DataImporterType {
    ZNPCS(() -> null), // TODO
    LEGACY_ZNPCS_PLUS(() -> null), // TODO
    CITIZENS(() -> null); // TODO

    private final LazyLoader<DataImporter> importerLoader;

    DataImporterType(LazyLoader.ObjectProvider<DataImporter> provider) {
        this.importerLoader = LazyLoader.of(provider);
    }

    DataImporter getImporter() {
        return importerLoader.get();
    }
}
