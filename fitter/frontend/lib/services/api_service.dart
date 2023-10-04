import 'dart:convert';

import 'package:fitter/models/month_daily_record.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiService {
  static const String baseUrl = "http://j9d202.p.ssafy.io:8000";
  late SharedPreferences prefs;

  Future<List> fetchEventsForMonth(DateTime day) async {
    final prefs = await SharedPreferences.getInstance();
    late final headers = {
      'Authorization': prefs.getString('Authorization').toString(),
    };
    const api = "api/calendar";
    final firstDayOfMonth =
        DateTime(day.year, day.month).toIso8601String().substring(0, 7);
    final uri = Uri.parse("$baseUrl/$api")
        .replace(queryParameters: {'date': firstDayOfMonth});
    final response = await http.get(uri, headers: headers);

    if (response.statusCode == 200) {
      final monthRecord = jsonDecode(utf8.decode(response.bodyBytes));
      List<dynamic> recordsList =
          monthRecord.map((item) => DailyMonthRecord.fromjson(item)).toList();
      return recordsList;
    } else {
      throw Error();
    }
  }

  Future<void> sendPostRequest({
    required String selectedDay,
    required Map type,
    required String detail,
    required String memo,
  }) async {
    final prefs = await SharedPreferences.getInstance();
    late final headers = {
      'Authorization': prefs.getString('Authorization').toString(),
      'Content-Type': 'application/json',
    };
    const String api = "api/calendar/write";
    final uri = Uri.parse("$baseUrl/$api");
    final response = await http.post(uri,
        headers: headers,
        body: jsonEncode({
          'date': selectedDay,
          'wodTypeDto': type,
          'detail': detail,
          'memo': memo,
        }));
    if (response.statusCode == 200) {
      // 서버가 성공적으로 응답하면.
      print('Data sent successfully : ${response.body}');
    } else {
      // 서버가 실패로 응답하면.
      print('Failed to send data : ${response.statusCode}');
    }
  }
}
