import 'dart:convert';

import 'package:fitter/models/wod_detail_model.dart';
import 'package:fitter/screens/wod/wod_input_screen.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class WodDetailScreen extends StatefulWidget {
  final String wodName, type, wodId;
  const WodDetailScreen(
      {super.key,
      required this.wodName,
      required this.type,
      required this.wodId});

  @override
  State<WodDetailScreen> createState() => _WodDetailScreenState();
}

class _WodDetailScreenState extends State<WodDetailScreen> {
  late Future<List<WODDetailModel>> wodDetail;
  late SharedPreferences prefs;

  @override
  void initState() {
    super.initState();
    callServer();
  }

  Future<List<WODDetailModel>> callServer() async {
    prefs = await SharedPreferences.getInstance();
    final accessToken = prefs.getString('Authorization');

    Map<String, String> headers = {
      'Authorization': accessToken.toString(),
    };

    var url = Uri.parse(
        'http://j9d202.p.ssafy.io:8000/api/named-wod/wod-record/list/${widget.wodName}');
    var response = await http.get(url, headers: headers);
    List<WODDetailModel> rmLists = [];

    if (response.statusCode == 200) {
      print('Response data: ${response.body}');
      final List<dynamic> rms = jsonDecode(response.body);
      for (var rm in rms) {
        rmLists.add(WODDetailModel.fromJson(rm));
      }
      return rmLists;
    }
    throw Error();
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
        // body: FutureBuilder(
        //   future: wodDetail,
        //   builder: (context, snapshot) {
        //     if (snapshot.hasData) {
        //       return Padding(
        //         padding: const EdgeInsets.symmetric(horizontal: 20),
        //         child: Column(
        //           crossAxisAlignment: CrossAxisAlignment.start,
        //           children: [
        //             Padding(
        //               padding:
        //                   const EdgeInsets.symmetric(vertical: 8, horizontal: 10),
        //               child: Row(
        //                 crossAxisAlignment: CrossAxisAlignment.center,
        //                 children: [
        //                   Text(
        //                     widget.wodName,
        //                     style: const TextStyle(
        //                         fontWeight: FontWeight.w900, fontSize: 20),
        //                   ),
        //                   const SizedBox(
        //                     width: 20,
        //                   ),
        //                 ],
        //               ),
        //             ),
        //             Flexible(
        //               flex: 5,
        //               child: CustomScrollView(
        //                 slivers: [
        //                   // Sliver 위젯들을 여기에 추가
        //                   SliverList(
        //                     delegate: SliverChildBuilderDelegate(
        //                       (BuildContext context, int index) {
        //                         return Padding(
        //                           padding:
        //                               const EdgeInsets.symmetric(vertical: 10),
        //                           child: detailButton(
        //                               snapshot.data![index].createDate,
        //                               snapshot.data![index].count.toString()),
        //                         );
        //                       },
        //                       childCount: snapshot.data!.length,
        //                     ),
        //                   ),
        //                 ],
        //               ),
        //             ),
        //             Flexible(
        //                 flex: 1,
        //                 child: Row(
        //                   mainAxisAlignment: MainAxisAlignment.end,
        //                   crossAxisAlignment: CrossAxisAlignment.center,
        //                   children: [
        //                     GestureDetector(
        //                       onTap: () {
        //                         Navigator.push(
        //                             context,
        //                             PageRouteBuilder(
        //                                 transitionsBuilder: (context, animation,
        //                                     secondaryAnimation, child) {
        //                                   var begin = const Offset(1.0, 0.0);
        //                                   var end = Offset.zero;
        //                                   var curve = Curves.ease;
        //                                   var tween = Tween(
        //                                           begin: begin, end: end)
        //                                       .chain(CurveTween(curve: curve));
        //                                   return SlideTransition(
        //                                     position: animation.drive(tween),
        //                                     child: child,
        //                                   );
        //                                 },
        //                                 pageBuilder: (context, anmation,
        //                                         secondaryAnimation) =>
        //                                     WodInputScreen(
        //                                         wodName: widget.wodName,
        //                                         type: widget.type,
        //                                         wodId: widget.wodId)));
        //                       },
        //                       child: const Icon(
        //                         Icons.add_box,
        //                         color: Color(0xff0080ff),
        //                         size: 50,
        //                       ),
        //                     ),
        //                   ],
        //                 ))
        //           ],
        //         ),
        //       );
        //     }

        //     return const Center(
        //       child: CircularProgressIndicator(),
        //     );
        //   },
        // ),
        );
  }

  Container detailButton(date, weight) {
    return Container(
      decoration: BoxDecoration(
          color: const Color(0xff0080ff),
          borderRadius: BorderRadius.circular(20)),
      width: 500,
      height: 63,
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              date,
              style: const TextStyle(color: Colors.white, fontSize: 20),
            ),
            Text(
              weight,
              style: const TextStyle(color: Colors.white, fontSize: 20),
            ),
            const Icon(
              Icons.arrow_forward_ios_rounded,
              color: Colors.white,
            ),
          ],
        ),
      ),
    );
  }
}
