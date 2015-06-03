#include <iostream>
#include <vector>
#include <string>
using namespace std;

bool isPalindrome(string str){
	return str == string(str.rbegin(), str.rend());
}

vector<string> getPermutations(string word){
	vector<string> permutations;
	string permutation = "";
	for (int i = 0; i < word.length(); i++)
	{
		permutation = "";
		permutation =  word.substr(i) + word.substr(0, i);
		permutations.push_back(permutation);
	}
	return permutations;
}

int main(){
	string word;
	vector<string> permutations;
	cin >> word;
	permutations.reserve(word.length());
	permutations = getPermutations(word);
	for (int i = 0; i < permutations.size(); i++)
	{
		if (isPalindrome(permutations[i])){
			cout << permutations[i] << endl;
		}
	}
	system("pause");
	return 0;
}