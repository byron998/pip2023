      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('graph'));
      //Y轴的数据，和数据值位置一一对应
      var catName = [
        "满江红",
        "流浪地球2",
        "无名",
        "流浪地球",
        "狂飙",
      ];
      //数据值，顺序和Y轴的名字一一对应
      var barData = [0, 0, 0, 0, 0];
      var option = {
        title: {
          text: "电影票房排行榜",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        //图表位置
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        //X轴
        xAxis: {
          type: "value",
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          //不显示X轴刻度线和数字
          splitLine: { show: false },
          axisLabel: { show: false },
        },
        yAxis: {
          type: "category",
          data: catName,
          //升序
          inverse: true,
          splitLine: { show: false },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          //key和图间距
          offset: 5,
          //动画部分
          animationDuration: 300,
          animationDurationUpdate: 300,
          //key文字大小
          nameTextStyle: {
            fontSize: 9,
            color: "black"
          },
        },
        series: [
          {
            //柱状图自动排序，排序自动让Y轴名字跟着数据动
            realtimeSort: true,
            name: "票房",
            type: "bar",
            data: barData,
            barWidth: 14,
            barGap: 10,
            smooth: true,
            valueAnimation: true,
            //Y轴数字显示部分
            label: {
              normal: {
                show: true,
                position: "right",
                valueAnimation: true,
                offset: [5, -2],
                textStyle: {
                  color: "#CD5C5C",
                  fontSize: 13,
                },
              },
            },
            itemStyle: {
              emphasis: {
                barBorderRadius: 7,
              },
              //颜色样式部分
              normal: {
                barBorderRadius: 7,
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: "#F08080" },
                  { offset: 1, color: "#B22222" },
                ]),
              },
            },
          },
        ],
        //动画部分
        animationDuration: 0,
        animationDurationUpdate: 3000,
        animationEasing: "linear",
        animationEasingUpdate: "linear",
      };
      myChart.setOption(option);
      //图表大小变动从新渲染，动态自适应
      window.addEventListener("resize", function () {
        myChart.resize();
      });
      
      var socket = new WebSocket('ws://localhost:8081/websocket?type=ws-reader&clientId=2048'); 
      socket.onopen = function(event){
          console.log(event);
      }
      socket.onclose = function(event){
          console.log(event);
      }
      socket.onerror = function(event){
          console.log(event);
      }
      socket.onmessage = function(event){
          console.log(event);
          var mess = event.data;
          console.log("message="+mess);
		      var movieName = mess.split(":")[0];
		      var movieData = mess.split(":")[1];
          for (var i=0;i < catName.length;i++) {
            if (catName[i] == movieName) {
              barData[i] = Number(movieData);
            }
          }
		  option.series[0].data = barData; //更新系列中的数据
          myChart.setOption(option); //图表重置
      }