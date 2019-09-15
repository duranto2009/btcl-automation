<style type="text/css">

</style>

<header class="main-header">
	<!-- Logo -->
	<a href="#" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels --> <span class="logo-mini"><img
			class="img-responsive" alt="" title="" src="<%=context%>assets/images/BTCL-small-Logo.png"> </span> <!-- logo for regular state and mobile devices -->
		<span class="logo-lg"><img class="img-responsive" alt="" title="" src="<%=context%>assets/images/BTCL-small-Logo.png">
	</span>
	</a>
	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top">
		<!-- Sidebar toggle button-->
		<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">Toggle navigation</span></a>
		<span class="header-text"> Bangladesh Telecommunications Company Ltd.</span>
		<span class="header-text-sm">BTCL</span>
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
				<!-- User Account: style can be found in dropdown.less -->
				<li class="dropdown user user-menu"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <img
						src="<%=context%>images/monsoor.jpg" class="user-image" alt="User Image"> <span class="hidden-xs"><%=loginDTO.getUsername()%></span>
				</a>
					<ul class="dropdown-menu">
						<!-- User image -->
						<li class="user-header"><img src="<%=context%>images/monsoor.jpg" class="img-circle" alt="User Image">

							<p>
								<%=loginDTO.getUsername()%>
							</p></li>
						<!-- Menu Footer-->
						<li class="user-footer">
							<div class="pull-left">
								<a href="#" class="btn btn-default btn-flat">Profile</a>
							</div>
							<div class="pull-right">
								<a href="../Logout.do" class="btn btn-default btn-flat">Sign out</a>
							</div>
						</li>
					</ul></li>
				<!-- Control Sidebar Toggle Button -->
				<li><a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a></li>
			</ul>
		</div>
	</nav>
</header>