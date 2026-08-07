package dev.darkblade.playerfurnaces.importer;

public class ImportResult {
    private final int importedCount;
    private final int skippedCount;
    private final boolean success;
    private final String errorMessage;

    public ImportResult(int importedCount, int skippedCount) {
        this.importedCount = importedCount;
        this.skippedCount = skippedCount;
        this.success = true;
        this.errorMessage = null;
    }

    public ImportResult(String errorMessage) {
        this.importedCount = 0;
        this.skippedCount = 0;
        this.success = false;
        this.errorMessage = errorMessage;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
