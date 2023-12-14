import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:ncdc/screen/auth/phone.dart';
import 'package:ncdc/screen/auth/verify.dart';
import 'package:ncdc/utils/firebase_options.dart';

void main() async{
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      initialRoute: 'phone',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      debugShowCheckedModeBanner: false,
      routes: {
        'phone': (context) =>  const MyPhone(),
        'verify': (context) => const MyVerify()
      },
    );
  }
}

