angular.module('logout', ['auth0', 'toastr'])
.controller( 'LogoutCtrl', function ($scope, $window, auth, $location, $window, store) {

  $scope.logout = function() {
    auth.signout();
    store.remove('profile');
    store.remove('delegationToken');
    store.remove('token');
    var destinationUrl = PARTNER_LOGIN_URL + '?externalReturnUrl=' + $window.location.origin;
    $window.location.href = destinationUrl;
  };

  $scope.logout();

});
