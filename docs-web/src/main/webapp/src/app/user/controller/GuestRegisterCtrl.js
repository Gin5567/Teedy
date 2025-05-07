angular.module('docs').controller('UserRequestListCtrl', function($scope, $http) {
    $scope.requests = [];

    function loadRequests() {
        $http.get('/api/userRequest/list')
            .then(function(response) {
                $scope.requests = response.data;
            }, function() {
                alert('Failed to load requests');
            });
    }

    $scope.approve = function(req) {
        $http.post('/api/userRequest/' + req.id + '/approve')
            .then(function() {
                req.status = 'accepted';
            }, function() {
                alert('Approve failed');
            });
    };

    $scope.reject = function(req) {
        $http.post('/api/userRequest/' + req.id + '/reject')
            .then(function() {
                req.status = 'rejected';
            }, function() {
                alert('Reject failed');
            });
    };

    loadRequests();
});
