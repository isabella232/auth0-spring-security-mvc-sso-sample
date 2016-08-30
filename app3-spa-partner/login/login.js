angular.module('app').controller('LoginCtrl', function ($scope, $location, auth, toastr) {
  $scope.auth = auth;
  if(!auth.config.sso) {
    $location.path('/logout');
  } else if (auth.config.sso && !auth.isAuthenticated) {
    auth.config.auth0js.login({
      scope: 'openid roles name email picture',
      response_type: 'token',
      sso: true,
      connections: [CONNECTION]
    });
  } else {
    $location.path('/home');
  }
});
