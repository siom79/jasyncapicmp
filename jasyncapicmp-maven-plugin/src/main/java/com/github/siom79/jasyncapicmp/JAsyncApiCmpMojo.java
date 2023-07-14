package com.github.siom79.jasyncapicmp;

import com.github.siom79.jasyncapicmp.configuration.Version;
import jasyncapicmp.cmp.ApiCompatibilityCheck;
import jasyncapicmp.cmp.AsyncApiComparator;
import jasyncapicmp.cmp.diff.ObjectDiff;
import jasyncapicmp.model.AsyncApi;
import jasyncapicmp.output.OutputProcessor;
import jasyncapicmp.output.StdoutOutputSink;
import jasyncapicmp.parser.AsyncApiParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mojo(name = "jasyncapicmp", defaultPhase = LifecyclePhase.VERIFY)
public class JAsyncApiCmpMojo extends AbstractMojo {

	@Parameter(required = true)
	private Version oldVersion;
	@Parameter(required = true)
	private Version newVersion;
	@Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		try {
			AsyncApiParser asyncApiParser = new AsyncApiParser();
			AsyncApi oldAsyncApi = asyncApiParser.parse(Files.readAllBytes(oldVersion.getFile().toPath()), oldVersion.getFile().getPath());
			AsyncApi newAsyncApi = asyncApiParser.parse(Files.readAllBytes(newVersion.getFile().toPath()), newVersion.getFile().getPath());
			AsyncApiComparator comparator = new AsyncApiComparator();
			ObjectDiff diff = comparator.compare(oldAsyncApi, newAsyncApi);
			ApiCompatibilityCheck apiCompatibilityCheck = new ApiCompatibilityCheck();
			diff = apiCompatibilityCheck.check(diff);
			StdoutOutputSink stdoutOutputTracker = new StdoutOutputSink();
			OutputProcessor stdoutYamlOutput = new OutputProcessor(stdoutOutputTracker);
			stdoutYamlOutput.process(diff);
			Path outputPath = Paths.get(outputDirectory.getPath(), "jasyncapicmp.txt");
			getLog().info("Writing output to " + outputPath);
			Files.write(outputPath, stdoutOutputTracker.toString().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
