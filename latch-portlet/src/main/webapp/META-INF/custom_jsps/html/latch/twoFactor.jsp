<!DOCTYPE html>
<html>
<head>
	<title>Latch Two Factor</title>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/latch-twofactor.css" media="screen" />
</head>
<body>
	<div class="two-factor__wrapper">
		<h1>Latch Two Factor</h1>
		<div class="two-factor__content">
			<form method="post">
				<div class="two-factor__content__logo"><img src="<%= request.getContextPath() %>/img/Latch.png" ></div>
				<p>This account is protected by Latch. Please enter the new password sent to your phone.</p>
				<input type="password" name="--token-value" value="" maxlength="6" class="two-factor__token">
				<input type="submit" value="OK" class="two-factor__send">
				<a class="two-factor__logout" href="${logoutUrl}">Return to home page</a>
			</form>
		</div>
	</div>
</body>
</html>