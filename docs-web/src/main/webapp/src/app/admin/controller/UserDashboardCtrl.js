'use strict';

angular.module('docs').controller('UserDashboardCtrl', function($scope, $http) {
  $scope.activities = [];
  console.log("✅ UserDashboardCtrl loaded!");

  $http.get('../api/admin/dashboard/activities').then(function(response) {
    $scope.activities = response.data;
    console.log("📦 Fetched activities:", $scope.activities);

    // 提取所有用户
    const users = [...new Set($scope.activities.map(a => a.creator.trim()))];
    console.log("🧑‍🤝‍🧑 Users detected:", users);

    // 为每个 documentId 生成唯一 hex 颜色
    const hashColor = (str) => {
      let hash = 0;
      for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
      }
      const color = (hash & 0x00ffffff).toString(16).toUpperCase();
      return "#" + "000000".substring(0, 6 - color.length) + color;
    };

    // 构造每个任务（颜色唯一）
    const tasks = $scope.activities.map(a => {
      const idx = users.indexOf(a.creator.trim());
      const color = hashColor(a.documentId); // 更换为真正唯一颜色
      return {
        name: a.title,
        value: [idx, a.createDate, new Date().toISOString()],
        itemStyle: { color }
      };
    });

    console.log("📊 Tasks for Gantt:", tasks);

    setTimeout(() => {
      const chartDom = document.getElementById('ganttChart');
      if (!chartDom) {
        console.error("❌ ganttChart DOM not found!");
        return;
      }

      const chart = echarts.init(chartDom);

      const option = {
        title: {
          text: 'User Upload Activity Gantt Chart'
        },
        tooltip: {
          formatter: params => {
            const creatorName = users[params.value[0]];
            return `${creatorName} → ${params.name}<br>${params.value[1]} → ${params.value[2]}`;
          }
        },
        xAxis: {
          type: 'time',
          name: 'Time'
        },
        yAxis: {
          type: 'category',
          name: 'User',
          data: users
        },
        series: [{
          type: 'custom',
          renderItem: function(params, api) {
            const categoryIndex = api.value(0);
            const start = api.coord([api.value(1), categoryIndex]);
            const end = api.coord([api.value(2), categoryIndex]);
            const height = 20;

            return {
              type: 'rect',
              shape: {
                x: start[0],
                y: start[1] - height / 2,
                width: end[0] - start[0],
                height: height
              },
              style: api.style()
            };
          },
          itemStyle: {
            opacity: 0.8
          },
          encode: {
            x: [1, 2]
          },
          data: tasks
        }]
      };

      console.log("🧾 Chart Option Preview:", option);
      chart.setOption(option);
    }, 100);
  });
});
