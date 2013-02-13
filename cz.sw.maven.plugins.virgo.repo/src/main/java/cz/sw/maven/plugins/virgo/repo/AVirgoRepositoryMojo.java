package cz.sw.maven.plugins.virgo.repo;

import java.io.File;

import org.apache.maven.plugin.dependency.AbstractDependencyFilterMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;

/**
 * Maven support mojo for developing Virgo applications with "pom-fist"
 * dependency model.
 * 
 * @author mzachar
 * 
 */
public abstract class AVirgoRepositoryMojo extends AbstractDependencyFilterMojo {

	/**
	 * Output repository directory
	 */
	@Parameter(defaultValue = "${user.home}/.m2virgo/", property = "repositoryDir", required = true)
	protected File repositoryDirectory;

	
	@Override
	protected ArtifactsFilter getMarkedArtifactFilter() {
		return null;
	}

}
