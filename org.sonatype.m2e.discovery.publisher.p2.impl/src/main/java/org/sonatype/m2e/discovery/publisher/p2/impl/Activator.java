package org.sonatype.m2e.discovery.publisher.p2.impl;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator
    implements BundleActivator
{
    public static final String PLUGIN_ID = "org.sonatype.m2e.discovery.publisher.p2.impl";

    private static Activator instance;

    private BundleContext context;

    public Activator()
    {
        Activator.instance = this;
    }

    @Override
	public void start( BundleContext context )
        throws Exception
    {
        this.context = context;
    }

    public static IProvisioningAgent newProvisioningAgent()
        throws ProvisionException
    {
        BundleContext context = getContext();

        ServiceReference<IProvisioningAgentProvider> providerRef = context.getServiceReference( IProvisioningAgentProvider.class );
        IProvisioningAgentProvider provider = context.getService( providerRef );
        try
        {
            return provider.createAgent( null ); // null == currently running system
        }
        finally
        {
            context.ungetService( providerRef );
        }
    }

    @Override
	public void stop( BundleContext context )
        throws Exception
    {
    }

    public static BundleContext getContext()
    {
        return instance.context;
    }
}
