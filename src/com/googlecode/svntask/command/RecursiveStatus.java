package com.googlecode.svntask.command;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;

import com.googlecode.svntask.Command;


/**
 * Used for executing svn status
 *
 * Available properties to set are:
 *  contentsStatusProperty = svn.status.contentsStatus
 *  toplevelPathsProperty = svn.status.toplevelPaths
 *
 * @author Linus Brimstedt
 */
public class RecursiveStatus extends Command
{
	private String path;

	private String contentsStatusProperty;
	private String toplevelPathsProperty;

	private Set<String> contentStatuses = new TreeSet<String>();
	private Set<String> toplevelPaths = new TreeSet<String>();

	private String basePath;
	
	
	/** */
	@Override
	public void execute() throws Exception
	{
		File filePath = new File(this.path);

		basePath = filePath.getCanonicalPath();
		this.getTask().log("recursive status " + basePath);

		// Get the WC Client
		SVNStatusClient client = this.getTask().getSvnClient().getStatusClient();

		// Execute svn status
		client.doStatus(filePath, null, SVNDepth.INFINITY, false, false, false, false, new ISVNStatusHandler() {
			
			@Override
			public void handleStatus(SVNStatus status) throws SVNException {
				File file = status.getFile();
				String fileName = file.getAbsolutePath().substring(basePath.length() + 1);
				if(shouldFileBeConsidered(file, fileName) )
				{
					String statusName = status.getContentsStatus().toString();
					contentStatuses.add(statusName);
					
					if(fileName.indexOf(File.separator) > 0)
					{
						toplevelPaths.add(fileName.substring(0, fileName.indexOf(File.separator)));
					}
					else
					{
						toplevelPaths.add(File.separator);
					}
					
					getTask().log("recursive status " + fileName + ": " + statusName);
				}
			}

			private boolean shouldFileBeConsidered(File file, String fileName)
			{
				return !file.isHidden() && !fileName.endsWith(".bak");
			}
		}, null);

		// Get the interesting status data
		if(contentStatuses.size() > 0)
		{
			this.getProject().setProperty(this.contentsStatusProperty, contentStatuses.toString());
			this.getProject().setProperty(this.toplevelPathsProperty, toplevelPaths.toString());
		}
	}

	@Override
	protected void validateAttributes() throws Exception
	{
		if (this.path == null)
			throw new Exception("path cannot be null");

		if (this.contentsStatusProperty == null)
			this.contentsStatusProperty = "svn.status.contentsStatus";

		if (this.toplevelPathsProperty == null)
			this.toplevelPathsProperty = "svn.status.toplevelPaths";
	}

	/**
	 * path to the file or directory
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * @param contentsStatusProperty The contentsStatusProperty to set. defaults to svn.status.contentsStatus
	 */
	public void setContentsStatusProperty(String contentsStatusProperty)
	{
		this.contentsStatusProperty = contentsStatusProperty;
	}

	/**
	 * @param toplevelPathsProperty The toplevelPathsProperty to set. defaults to svn.status.toplevelPaths
	 */
	public void setToplevelPathsProperty(String toplevelPathsProperty)
	{
		this.toplevelPathsProperty = toplevelPathsProperty;
	}
}
