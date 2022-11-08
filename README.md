<div>
<h3>Code clipboard</h3>
<p>A simple code storage that allows user to submit, store and efficiently query code samples.
<p>The clipboard provides methods for querying samples by programming language, a set of tags assigned to the code, code substrings, and any possible combinations of the given options.


<style>
ul {
  margin: 0;
}
ul.dashed {
  list-style-type: none;
}
ul.dashed > li {
  text-indent: -5px;
  margin-top: 10px;
}
ul > li{
  margin-top: 10px;
}
ul.dashed > li:before {
  content: "-";
  text-indent: -5px;
}
</style>
<p>Available Spring profiles:
<ul class="dashed">
    <li> hibernate: Enable JPA Hibernate Code repository implementation</li>
    <li> suffixtree: Use an alternative code repository based on the GeneralizedSuffixTree (see https://github.com/abahgat/suffixtree)</li>
    <li> default: Default Spring profile</li>
</ul>

<p>Application endpoints:
<ul>
    <li>(GET) $host name$/ : Index webpage</li>
    <li>(GET) $host name$/search : Search (code) webpage</li>
    <li>(GET) $host name$/submit : Submit (code) webpage</li>
    <li>(POST) $host name$/send-code : Send a new Code sample in JSON format</li>
    <li>(POST) $host name$/search : Search for code samples by specified parameters</li>
    <li>(POST) $host name$/quit : Kill application server</li>
</ul>
</div>

<p>Preview:</p>
<video src="https://user-images.githubusercontent.com/62184786/197869099-1c58b588-3405-4d6b-b385-122b84ca3a60.mp4"></video>
