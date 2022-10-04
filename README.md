<div>
<h3>Code clipboard</h3>
<h5>A simple code clipboard that allows user to submit, store and efficiently query code samples.</h5>
<h5>The clipboard allows user to query code samples by a programming language, a set of tags, assigned to the code, substrings of code samples, and any combination of the above options. This application uses temporary file-stored database to store code samples.</h5>
<h5>The file-stored database uses <b>GeneralizedSuffixTree</b></h5>(see https://github.com/abahgat/suffixtree)

<h4>Application endpoints:</h4>
<ul>
<li>(GET) / Index page</li>
<li>(GET) /search Search webpage</li>
<li>(GET) /submit Submit webpage</li>
<li>(POST) /send-code Send a Code sample</li>
<li>(POST) /search Search code samples with options</li>
</ul>
</div>

<video src="https://user-images.githubusercontent.com/62184786/193868678-23d5d57c-3f93-403f-a0c4-da08d3444a0d.mp4" controls="false" autoplay="autoplay"></video>
