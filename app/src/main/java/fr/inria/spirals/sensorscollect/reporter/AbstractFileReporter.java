package fr.inria.spirals.sensorscollect.reporter;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.reporter.AbstractReporter;
import fr.inria.spirals.sensorscollect.api.reporter.ReporterInitializationFailure;
import fr.inria.spirals.sensorscollect.api.toggle.ToggleException;

public abstract class AbstractFileReporter extends AbstractReporter {

    private static final Set<String> REQUIRED_PERMISSIONS = new HashSet<String>() {
        {
            add(Manifest.permission.READ_EXTERNAL_STORAGE);
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    };

    private static final String OUTPUT_DIRECTORY_NAME = "sensors-collect";

    private final String outputFilePrefix;
    private String outputRootDirectoryPath;
    private PrintWriter outputFileWriter;
    private PrintWriter outputErrorFileWriter;

    public AbstractFileReporter(@NonNull final String outputFilePrefix) {
        this.outputFilePrefix = outputFilePrefix;
    }

    public PrintWriter getOutputFileWriter() {
        return outputFileWriter;
    }

    public PrintWriter getOutputErrorFileWriter() {
        return outputErrorFileWriter;
    }

    public abstract String getOutputFileExtension();

    public abstract String getErrorOutputFileExtension();

    @Override
    protected boolean canStart() {
        try {
            initOutputRootDirectoryPath();
            return true;
        } catch (final ReporterInitializationFailure e) {
            return false;
        }
    }

    @Override
    protected void doStart() {
        final long now = new Date().getTime();
        try {
            initOutputFileWriter(outputRootDirectoryPath, now);
            initOutputErrorFileWriter(outputRootDirectoryPath, now);
        } catch (final ReporterInitializationFailure e) {
            throw new ToggleException("Unable to start reporter", e);
        }
    }

    @Override
    protected void doStop() {
        if (outputFileWriter == null) {
            throw new ToggleException("Unable to stop null output file writer");
        }
        outputFileWriter.close();
        if (outputErrorFileWriter == null) {
            throw new ToggleException("Unable to stop null output error file writer");
        }
        outputErrorFileWriter.close();
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>(REQUIRED_PERMISSIONS);
    }

    private void initOutputRootDirectoryPath() {
        if (!isExternalStorageWritable()) {
            throw new ReporterInitializationFailure("External storage is not writable");
        }
        final File outputRootDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), OUTPUT_DIRECTORY_NAME);
        if (!outputRootDirectory.exists()) {
            if (!outputRootDirectory.mkdirs()) {
                throw new ReporterInitializationFailure("Unable to create output root directory " + OUTPUT_DIRECTORY_NAME + " from the external storage public directory");
            }
        }
        this.outputRootDirectoryPath = outputRootDirectory.getAbsolutePath();
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void initOutputFileWriter(final String outputRootDirectoryPath, final long timestamp) {
        final String outputFilePath = String.format(
                "%s%s%s-%d.%s",
                outputRootDirectoryPath,
                File.separator,
                outputFilePrefix,
                timestamp,
                getOutputFileExtension()
        );
        try {
            outputFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)));
        } catch (IOException e) {
            throw new ReporterInitializationFailure("Unable to create output file", e);
        }
    }

    private void initOutputErrorFileWriter(final String outputRootDirectoryPath, final long timestamp) {
        final String outputErrorFilePath = String.format(
                "%s%s%s-error-%d.%s",
                outputRootDirectoryPath,
                File.separator,
                outputFilePrefix,
                timestamp,
                getErrorOutputFileExtension()
        );
        try {
            outputErrorFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputErrorFilePath, true)));
        } catch (IOException e) {
            throw new ReporterInitializationFailure("Unable to create output file", e);
        }
    }

}
