import 'package:fitter/screens/sign_up/additional_box.dart';
import 'package:fitter/screens/sign_up/additional_privacy.dart';
import 'package:fitter/widgets/button_mold.dart';
import 'package:fitter/widgets/empty_box.dart';
import 'package:fitter/widgets/input_text.dart';
import 'package:flutter/material.dart';

class AdditionalInfo extends StatelessWidget {
  const AdditionalInfo({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 0,
        foregroundColor: Colors.white,
        backgroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 30),
        child: Column(
          children: [
            const EmptyBox(boxSize: 2),
            const Row(
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Fitter가 되신 것을 환영합니다.",
                      style: TextStyle(fontWeight: FontWeight.w500),
                    ),
                    Text(
                      "정보를 입력해주세요.",
                      style: TextStyle(fontSize: 30),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(
                width: 500,
                child: Divider(color: Color(0xFF0080FF), thickness: 3)),
            const EmptyBox(boxSize: 1),
            Center(
              child: Flexible(
                flex: 3,
                child: Padding(
                  padding: const EdgeInsets.all(80),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(180),
                    child: Image.asset(
                      "assets/images/default_user_img.jpg",
                    ),
                  ),
                ),
              ),
            ),
            const Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Flexible(
                  flex: 3,
                  child: InputText(hintText: "닉네임"),
                ),
                Flexible(
                  flex: 1,
                  child: ButtonMold(
                    btnText: " 중복 확인 ",
                    verticalLength: 10,
                    horizontalLength: 5,
                    buttonColor: true,
                  ),
                ),
              ],
            ),
            const EmptyBox(boxSize: 1),
            GestureDetector(
              onTap: () {
                Navigator.push(
                    context,
                    PageRouteBuilder(
                      transitionsBuilder:
                          (context, animation, secondaryAnimation, child) {
                        var begin = const Offset(1.0, 0.0);
                        var end = Offset.zero;
                        var curve = Curves.ease;
                        var tween = Tween(begin: begin, end: end)
                            .chain(CurveTween(curve: curve));
                        return SlideTransition(
                          position: animation.drive(tween),
                          child: child,
                        );
                      },
                      pageBuilder: (context, anmation, secondaryAnimation) =>
                          const AdditionalPrivacy(),
                    ));
              },
              child: const ButtonMold(
                btnText: "다 음 으 로",
                horizontalLength: 25,
                verticalLength: 10,
                buttonColor: true,
              ),
            ),
            const EmptyBox(boxSize: 4),
          ],
        ),
      ),
    );
  }
}
