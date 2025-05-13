'use strict';

angular.module('docs').controller('GuestRegisterCtrl', function($scope, $http) {
    $scope.name = '';
    $scope.email = '';
    $scope.message = '';
    $scope.success = false;

    $scope.submitRequest = function() {
        $http({
            method: 'POST',
            url: '../api/userRequest/submit',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            transformRequest: function(obj) {
                const str = [];
                for (let p in obj)
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                return str.join("&");
            },
            data: {
                name: $scope.name,
                email: $scope.email
            }
        }).then(function() {
            $scope.message = "Registration submitted!";
            $scope.success = true;
        }, function() {
            $scope.message = "Failed to submit registration.";
            $scope.success = false;
        });

    };
});
