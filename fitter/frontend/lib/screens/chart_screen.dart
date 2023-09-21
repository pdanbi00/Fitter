import 'package:fitter/widgets/button_mold.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:syncfusion_flutter_charts/charts.dart';
import 'package:syncfusion_flutter_charts/sparkcharts.dart';

class ChartScreen extends StatelessWidget {
  const ChartScreen({super.key});

  @override
  Widget build(BuildContext context) {
    List<ChartData> chartData = [
      ChartData(DateTime(2022, 1, 1), 10.0),
      ChartData(DateTime(2022, 3, 2), 50.8),
      ChartData(DateTime(2022, 5, 3), 30.0),
      ChartData(DateTime(2022, 7, 4), 20.0),
      ChartData(DateTime(2022, 9, 3), 38.0),
      ChartData(DateTime(2022, 11, 24), 45.0),
      ChartData(DateTime(2023, 1, 3), 27.0),
      ChartData(DateTime(2023, 3, 4), 29.0),
    ];
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 0,
        foregroundColor: const Color(0xFF0080FF),
        backgroundColor: Colors.white,
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding:
                    const EdgeInsets.symmetric(vertical: 8, horizontal: 20),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const Text(
                      "운동종목",
                      style:
                          TextStyle(fontWeight: FontWeight.w900, fontSize: 25),
                    ),
                    const SizedBox(
                      width: 20,
                    ),
                    Container(
                      decoration: const BoxDecoration(color: Colors.deepPurple),
                      width: 30,
                      height: 20,
                    ),
                  ],
                ),
              ),
              Container(
                  decoration: const BoxDecoration(color: Colors.white),
                  child: SfCartesianChart(
                      plotAreaBackgroundColor: Colors.blueGrey.shade50,
                      // backgroundColor: Colors.blueGrey.shade50,
                      // 차트 부분만 회색으로 할 지, 아니면 숫자 부분까지 다 회색으로 할 지 고민
                      primaryXAxis: DateTimeAxis(
                        dateFormat: DateFormat('MM / dd'), // 연도하면 차트부분 너무 커짐...
                        labelIntersectAction:
                            AxisLabelIntersectAction.multipleRows,
                        // majorGridLines:
                        //     const MajorGridLines(width: 0),
                        // majorTickLines:
                        //     const MajorTickLines(size: 0), // 레이블과 눈금 사이의 간격을 조절
                        // minorGridLines:
                        //     const MinorGridLines(width: 0), // 마이너 그리드 라인 숨김
                        minorTickLines: const MinorTickLines(size: 0),
                        // labelRotation: 305,
                        // interval: chartData.length > 3
                        //     ? (chartData.length / 3) * 2
                        //     : chartData.length * 20,
                      ),
                      series: <ChartSeries>[
                        StackedLineSeries<ChartData, DateTime>(
                          dataLabelSettings: const DataLabelSettings(
                            isVisible: true,
                            useSeriesColor: true,
                          ),
                          dataSource: chartData,
                          xValueMapper: (ChartData data, _) => data.x,
                          yValueMapper: (ChartData data, _) => data.y,
                        )
                      ])),
              detailMRButton(),
              const SizedBox(
                height: 20,
              ),
              detailMRButton(),
              const SizedBox(
                height: 20,
              ),
              detailMRButton(),
              const SizedBox(
                height: 20,
              ),
              detailMRButton(),
              const SizedBox(
                height: 20,
              ),
              detailMRButton(),
              const SizedBox(
                height: 20,
              ),
              detailMRButton(),
            ],
          ),
        ),
      ),
    );
  }

  Container detailMRButton() {
    return Container(
      decoration: BoxDecoration(
          color: const Color(0xff0080ff),
          borderRadius: BorderRadius.circular(25)),
      width: 500,
      height: 70,
      child: const Padding(
        padding: EdgeInsets.all(20),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              "2023.09.20",
              style: TextStyle(color: Colors.white, fontSize: 20),
            ),
            Text(
              "180lb",
              style: TextStyle(color: Colors.white, fontSize: 20),
            ),
            Icon(
              Icons.arrow_forward_ios_rounded,
              color: Colors.white,
            ),
          ],
        ),
      ),
    );
  }
}

class ChartData {
  final DateTime x;
  final double y;

  ChartData(this.x, this.y);
}