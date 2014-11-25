#Liferay Latch plugin
====================

With latch liferay plugin you can add a new security level to your Liferay instances. Latch is a delegation security system that let to your users configure your own security mechanism and use their mobile phone as second factor security.

Latch plugin is simple to configure and use, is not intrusive, only adds a new security layer without any modifying in your base installation, it can be disabled or removed without any problem.

Latch plugin supports all security mode available in Latch service. These security modes are configurable per every user:
- **Lock/Unlock**. Simplest mode that let to the user lock their account access from their mobile with a single click.
- **Autolock by time**. The user account will be locked after the configured time.
- **Autolock by use**. Once the user will be logged the account will be locked.
- **Schedule lock**. The user can configure the period in which the account will be locked. For example: from 8pm to 8am the acces to my account will be locked.
- **One time password**. The user will receive a second password in their mobile to validate the access to the portal.

##Prerequisites
- Liferay 6.2 (6.1 in test)
- Latch developer account. To configure the plugin do you needs to create an application in Latch developer area to get an account id and secret key. In the application configuration also you can configure if the OTP (one time password) is available to your users.

##Installing and configuring Latch plugin
Soon the first version will be released.

To install the plugin, you only needs to copy the .war package into the Liferay deploy directory.
To configure the plugin you need to add the Latch Portlet to any page and access to the portlet configuration (engine icon) where you can specify the required parameters to integrate your portal with Latch.

Your portal is ready!

##Latch plugin usage
- **Pairing an account**. The users can access to the page where the portlet is deployed and if their account are not paired with Latch the "Pair account" button will be displayed.
- **Unpairing an account**. If account is paired the same portlet will display the "Unpair" option.

After that, if an account is managed by Latch on the login process the account status will be checked in Latch and act conform the status, allowing or denying the access to the portal.
