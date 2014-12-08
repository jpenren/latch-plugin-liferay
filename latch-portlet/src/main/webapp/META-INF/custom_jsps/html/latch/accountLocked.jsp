<!DOCTYPE html>
<html>
<head>
	<title>Account locked</title>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/latch-style.css" media="screen" />
</head>
<body>
	<div class="two-factor__wrapper">
		<h1>Account locked</h1>
		<div class="two-factor__content">
			<form method="post">
				<div class="two-factor__content__logo"><img src="<%= request.getContextPath() %>/img/Latch.png" ></div>
				<p>This account is protected by Latch. Please unlock the account in your phone.</p>
				<a href="${logoutUrl}">Return to home page</a>
			</form>
		</div>
	</div>
</body>
</html>
