package cz.sw.maven.plugins.virgo.repo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Maven support mojo for developing Virgo applications with "pom-fist" dependency
 * model.
 * 
 * Clean virgo repository. Usefull when removing from dependency from pom so it will also be removed from repository
 * 
 * @author matej zachar
 *
 */
@Mojo(name="clean", defaultPhase=LifecyclePhase.CLEAN, instantiationStrategy=InstantiationStrategy.SINGLETON, threadSafe=true)
public class VirgoRepositoryCleanMojo extends AVirgoRepositoryMojo {

	private AtomicBoolean wasExecuted = new AtomicBoolean(false);
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		// execute only once per build
		if (wasExecuted.getAndSet(true)) {
			getLog().debug("Skipping repository clean "+repositoryDirectory.getAbsolutePath()+" <was already cleaned>");
			return;
		}
		
		try {
			if (repositoryDirectory == null || repositoryDirectory.exists() == false)
				return;
			
			FileUtils.cleanDirectory(repositoryDirectory);
			getLog().info("Cleaned repository "+repositoryDirectory.getAbsolutePath());
		} catch (IOException e) {
			getLog().warn("Unable to clean repositoryDirectory", e);
		}
	}
	
}
