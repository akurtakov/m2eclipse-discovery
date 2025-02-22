package org.sonatype.m2e.discovery.publisher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.tycho.osgi.runtime.TychoOsgiRuntimeArtifacts;

@Component( role = TychoOsgiRuntimeArtifacts.class, hint = "xx" )
public class P2RuntimeMetadata
    implements TychoOsgiRuntimeArtifacts
{
    private static final String DEFAULT_VERSION = "0.4.0";

    private static final List<Dependency> ARTIFACTS;

    @Requirement
    private Logger log;

    static
    {
        ARTIFACTS = new ArrayList<Dependency>();

        String p2Version = getVersion();

        ARTIFACTS.add( newDependency( "io.takari.m2e.discovery.publisher",
                                      "org.sonatype.m2e.discovery.publisher.p2.facade", p2Version, "jar" ) );
        ARTIFACTS.add( newDependency( "io.takari.m2e.discovery.publisher",
                                      "org.sonatype.m2e.discovery.publisher.p2.impl", p2Version, "jar" ) );
    }

    public List<Dependency> getRuntimeArtifacts()
    {
        log.debug( "P2RuntimeMetadata.getRuntimeArtifacts(): " + ARTIFACTS );
        return ARTIFACTS;
    }

    private static Dependency newDependency( String groupId, String artifactId, String version, String type )
    {
        Dependency d = new Dependency();
        d.setGroupId( groupId );
        d.setArtifactId( artifactId );
        d.setVersion( version );
        d.setType( type );
        return d;
    }

    public static String getVersion()
    {
        ClassLoader cl = TychoOsgiRuntimeArtifacts.class.getClassLoader();
        InputStream is =
            cl.getResourceAsStream( "META-INF/maven/org.sonatype.m2e.discovery.publisher/org.sonatype.m2e.discovery.publisher.maven-plugin/pom.properties" );
        String version = DEFAULT_VERSION;
        if ( is != null )
        {
            try
            {
                try
                {
                    Properties p = new Properties();
                    p.load( is );
                    version = p.getProperty( "version", null );
                }
                finally
                {
                    is.close();
                }
            }
            catch ( IOException e )
            {
                throw new RuntimeException( "Could not read pom.properties:" + e.getMessage(), e );
            }
        }
        return version;
    }
}
