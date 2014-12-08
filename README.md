#Latch Plugin for Liferay

With latch liferay plugin you can add a new security layer to your Liferay instances. [Latch](https://latch.elevenpaths.com) is a security system that let to your users configure your own security mechanism and use their mobile phone as second factor security. Latch plugin is simple to configure and use, is not intrusive, only adds a new security layer without any modifying in your base installation, it can be disabled or removed without any problem.

Latch plugin supports all security mode available in Latch service. These security modes are configurable per every user:
- **Lock/Unlock**. Simplest mode that let to the user lock their account access from their mobile with a single click.
- **Autolock by time**. The user account will be locked after the configured time.
- **Autolock by use**. Once the user will be logged the account will be locked.
- **Schedule lock**. The user can configure the period in which the account will be locked. For example: from 8pm to 8am the acces to my account will be locked.
- **One time password**. The user will receive a second password in their mobile to validate the access to the portal.

<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L03.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L03.png" width="200px">
</a>
<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L01.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L01.png" width="200px">
</a>
<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L04.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/L04.png" width="200px">
</a>

##Prerequisites
- Liferay 6.2 (testing 6.1)
- Latch developer account. To configure the plugin do you needs to create an application in [Latch developer area](https://latch.elevenpaths.com/www/) to get an application id and secret key. Also, in the application configuration page, you can configure if the OTP (one time password) is available.

##Installing and configuring Latch plugin

To install the plugin, you only need to copy the .war package (downloaded from release tab) into the Liferay deploy directory.
To configure the plugin you need to add the Latch Portlet to any page and access to the portlet configuration (engine icon) where you can specify the required parameters to integrate your portal with Latch.

<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/Configuration.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/Configuration.png" width="400px">
</a>

Your portal is ready!

##Latch plugin usage
- **Pairing an account**. The users can access to the page where the portlet is deployed and if their account are not paired with Latch the "Pair account" button will be displayed.
- **Unpairing an account**. If account is paired the same portlet will display the "Unpair" option.

After that, if an account is managed by Latch on the login process the account status will be checked in Latch and act conform the status, allowing or denying the access to the portal. If the account has one time password feature activated then a new validation page will be shown to enter the password sended to the user's mobile.

<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/Pairing.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/Pairing.png" width="350px">
</a>
<a href="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/TwoFactor.png">
<img src="https://raw.githubusercontent.com/jpenren/latch-plugin-liferay/master/src/site/doc/TwoFactor.png" width="350px">
</a>
