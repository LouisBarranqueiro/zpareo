<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>ZPareo - Authentification</title>
		<link type="text/css" rel="stylesheet" href="http://localhost:8080/ZPareo/ressources/css/bootstrap.css" />
        <link type="text/css" rel="stylesheet" href="http://localhost:8080/ZPareo/ressources/css/jquery-ui.custom.min.css" />
        <link type="text/css" rel="stylesheet" href="http://localhost:8080/ZPareo/ressources/css/main.css" />
        <link type="text/css" rel="stylesheet" href="http://localhost:8080/ZPareo/ressources/css/style.css" />
	</head>
	<body>
			<div id="site-conteneur">
				<header>
					<nav>
						<a href="">ZPareo</a>
					</nav>
				</header>
				<main>
					<div id="empty"></div>
					<div id="connexion" class="module fond-gris">
						<div class="module-barre fond-gris">
							<h1 class="centre">Authentification</h1>
						</div>
						<form id="authentification" action="<c:url value='/connexion'/>" method="POST" class="form-horizontal" role="form">
						<div class="module-form">						
							<input type="text" name="adresseMail" class="form-control input-sm" value="<c:out value='${ utilisateur.adresseMail }'></c:out>" placeholder="Adresse mail" pattern="[a-zA-Z0-9@.-_]+@[a-zA-Z]{2,20}.[a-zA-Z]{2,3}" x-moz-errormessage="Veuillez entrez un identifiant" required/>
							<input type="password" name="motDePasse" class="form-control input-sm" placeholder="Mot de passe" pattern=".{8,}" x-moz-errormessage="Veuillez entrez un mot de passe" required/>
							<span class="erreur">${ form.erreurs['connexion'] }</span>
							<button type="submit" class="form-control  bouton bouton-primary">CONNEXION</button>
						</div>
						<div class="loader-conteneur">
							<img class="loader"src="<c:url value="/ressources/img/ajax-loader.gif"/>"/>
						</div>
						</form>
					</div>
				</main>
			</div><!-- Fin site-conteneur -->
	<script type="text/javascript" src="http://localhost:8080/ZPareo/ressources/js/jquery.js"></script>
    <script type="text/javascript" src="http://localhost:8080/ZPareo/ressources/js/jquery-ui.custom.min.js"></script>
	<script type="text/javascript" src="http://localhost:8080/ZPareo/ressources/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="http://localhost:8080/ZPareo/ressources/js/jquery.ui.datepicker-fr.js"></script> 
	<script type="text/javascript" src="http://localhost:8080/ZPareo/ressources/js/script.js"></script> 
	</body>
</html>