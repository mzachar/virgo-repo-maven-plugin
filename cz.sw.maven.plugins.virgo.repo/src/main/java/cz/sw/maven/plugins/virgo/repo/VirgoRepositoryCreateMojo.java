package cz.sw.maven.plugins.virgo.repo;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Maven support mojo for developing Virgo applications with "pom-fist" dependency
 * model.
 * 
 * This mojo will copy reactor build dependencies to
 * {@link #repositoryDirectory} (default: ${user.home}/.m2virgo/) folder so they
 * can be easily consumed by Virgo by simple adding {@link #repositoryDirectory} as watched directory
 * 
 * @author matej zachar
 */
@Mojo(name = "create", defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresDependencyResolution = ResolutionScope.TEST, instantiationStrategy=InstantiationStrategy.SINGLETON, threadSafe=true)
public class VirgoRepositoryCreateMojo extends AVirgoRepositoryMojo {

	// contains set of already copied artifacts
	private Set<Artifact> alreadyCopiedArtifacts = Collections.synchronizedSet(new HashSet<Artifact>());
	
	public void execute() throws MojoExecutionException {
		// force to always exclude provided scope
		this.excludeScope = "provided";

		// prepare outputDirecotry
		if (repositoryDirectory.exists() == false) {
			repositoryDirectory.mkdirs();
		}

		// resolve artifact and copy them to repositoryDirectory
		Set<Artifact> artifacts = getResolvedDependencies(true);
		for (Artifact artifact : artifacts) {
			File artifactFile = artifact.getFile();

			// skip workspace artifacts (or any which is directory)
			if (artifactFile.isDirectory())
				continue;

			// copy only once if more reactor projects use the same artifact
			if (alreadyCopiedArtifacts.add(artifact)) {
				copyFile(artifactFile, new File(repositoryDirectory, artifactFile.getName()));
			} else {
				getLog().debug("Skipping "+( this.outputAbsoluteArtifactFilename ? artifactFile.getAbsolutePath() : artifactFile.getName() )+" <was already copied>");
			}
		}
	}
	
}
