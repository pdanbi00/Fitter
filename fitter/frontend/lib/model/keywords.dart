class KeywordModel {
  final String keyword;

  KeywordModel.fromJson(Map<String, dynamic> json) : keyword = json['name'];
}
