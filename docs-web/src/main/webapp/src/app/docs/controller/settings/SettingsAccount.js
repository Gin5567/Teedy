'use strict';

/**
 * Settings account controller.
 */
angular.module('docs').controller('SettingsAccount', function($scope, $rootScope, Restangular, $translate) {
  $scope.editUserAlert = false;
  $scope.alerts = [];

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.editUser = function() {
    Restangular.one('user').post('', $scope.user).then(function() {
      $scope.user = {};
      $scope.alerts.push({ type: 'success', msg: $translate.instant('settings.account.updated') });
    });
  };

  $scope.getUserRole = function() {
    const user = $rootScope.userInfo;

    if (!user || user.anonymous) {
      return 'guest';
    }

    if (user.base_functions && user.base_functions.indexOf('ADMIN') !== -1) {
      return 'admin';
    }

    if (user.username && user.username.toLowerCase().startsWith('guest')) {
      return 'guest';
    }

    return 'user';
  };

  $scope.isAdmin = function() {
    return $scope.getUserRole() === 'admin';
  };

  $scope.isGuest = function() {
    return $scope.getUserRole() === 'guest';
  };

  $scope.isUser = function() {
    return $scope.getUserRole() === 'user';
  };
});
