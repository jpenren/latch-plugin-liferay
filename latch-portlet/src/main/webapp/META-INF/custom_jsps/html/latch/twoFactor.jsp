<!DOCTYPE html>
<html>
<head>
	<title>Latch Two Factor</title>
	<style type="text/css">
	<!--
	body{
		width: 100%;
		text-align: center;
		font-weight: 200;
		font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
	}
	.two-factor__wrapper{
		color: #555;
		font-size: 14px;
		line-height: 20px;
		border:1px solid #eaeaea;
		border-top-width: 0;
		padding: 12px 10px 10px;
		width: 500px;
		margin-left: auto;
		margin-right: auto;
		text-align: left;
		padding: 0;
		margin-top: 4em;
	}
	.two-factor__wrapper h1{
		background: #d3d3d3;
		color: #FFF;
		padding: .4em;
		margin: 0;
		font-size: 1em;
	}
	.two-factor__content{
		padding: 2em;
	}
	.two-factor__token{
		padding: 4px 12px;
		margin-bottom: 0;
		font-size: 14px;
		line-height: 20px;
		text-align: center;
		border: 1px solid #cfcfcf;
		border-bottom-color: #b5b5b5;
	}
	.two-factor__send{
		color: white;
		background-color: #4a96e8;
		margin-top: 1em;
		text-shadow: 0 -1px 0 rgba(0,0,0,0.25);
		background-image: linear-gradient(to bottom,#54aaff,#4a96e8);
		border-color: rgba(0,0,0,0.1) rgba(0,0,0,0.1) rgba(0,0,0,0.25);
		padding: 4px 12px;
		margin-bottom: 0;
		font-size: 14px;
		line-height: 20px;
		text-align: center;
		cursor: pointer;
		border-radius: 2px;
		box-shadow: inset 0 1px 0 rgba(255,255,255,0.2),0 1px 2px rgba(0,0,0,0.05);
	}
	.two-factor__logout{
		margin-left: 5em;
	}
	-->
	</style>
</head>
<body>
	<div class="two-factor__wrapper">
		<h1>Latch Two Factor</h1>
		<div class="two-factor__content">
			<form method="post">
				<p>This account is protected by Latch. Please enter the new password sent to your phone.</p>
				<input type="text" name="--token-value" value="" maxlength="6" class="two-factor__token">
				<input type="submit" value="OK" class="two-factor__send">
				<a class="two-factor__logout" href="${logoutUrl}">Return to home page</a>
			</form>
		</div>
	</div>
</body>
</html>