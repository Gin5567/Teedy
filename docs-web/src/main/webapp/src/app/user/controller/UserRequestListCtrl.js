'use strict';

angular.module('docs').controller('UserRequestListCtrl', function($scope, $http) {
    // 初始化请求数组
    $scope.requests = [];

    // 加载所有请求
    function loadRequests() {
        $http.get('../api/userRequest/list')
            .then(function(response) {
                $scope.requests = response.data;
            }, function(error) {
                console.error('加载用户请求失败:', error);
                alert('无法加载用户请求');
            });
    }

    // 批准请求
    $scope.approve = function(req) {
        $http.post('../api/userRequest/' + req.id + '/approve')
            .then(function() {
                req.status = 'accepted';
            }, function(error) {
                console.error('批准失败:', error);
                alert('批准失败');
            });
    };

    // 拒绝请求
    $scope.reject = function(req) {
        $http.post('../api/userRequest/' + req.id + '/reject')
            .then(function() {
                req.status = 'rejected';
            }, function(error) {
                console.error('拒绝失败:', error);
                alert('拒绝失败');
            });
    };

    // 页面加载时调用
    loadRequests();
});
