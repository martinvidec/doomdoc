document.addEventListener('DOMContentLoaded', function() {
    var toggler = document.getElementsByClassName("caret");
    for (var i = 0; i < toggler.length; i++) {
        toggler[i].addEventListener("click", function() {
            this.parentElement.querySelector(".nested").classList.toggle("active");
            this.classList.toggle("caret-down");
        });
    }
});

function search() {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById('search');
    filter = input.value.toUpperCase();
    ul = document.getElementById("packageTree");
    li = ul.getElementsByTagName('li');

    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("span")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
            expandParent(li[i]);
        } else {
            li[i].style.display = "none";
        }
    }

    // Ensure parent nodes of matching elements are visible
    for (i = 0; i < li.length; i++) {
        if (li[i].style.display === "") {
            expandParent(li[i]);
        }
    }
}

function expandParent(element) {
    var parent = element.parentElement;
    while (parent && parent.id !== "packageTree") {
        if (parent.classList.contains("nested")) {
            parent.classList.add("active");
            parent.previousElementSibling.classList.add("caret-down");
        }
        parent.style.display = "";
        parent = parent.parentElement;
    }
}

function showDoc(className) {
    var docContent = document.getElementById('docContent');
    var docTitle = document.getElementById('docTitle');
    docTitle.innerHTML = className;
    // Fetch and display the documentation for the selected class
    // This is a placeholder, replace with actual documentation content
    docContent.innerHTML = '<p>Documentation for ' + className + '</p>';
}

function generateTree(packages) {
    var ul = document.getElementById("packageTree");
    for (var packageName in packages) {
        if (packages.hasOwnProperty(packageName)) {
            var packageNode = document.createElement("li");
            var caret = document.createElement("span");
            caret.className = "caret";
            caret.textContent = packageName;
            packageNode.appendChild(caret);

            var nestedUl = document.createElement("ul");
            nestedUl.className = "nested";
            packages[packageName].forEach(function(className) {
                var classNode = document.createElement("li");
                var classElement = document.createElement("span");
                classElement.className = "element";
                classElement.textContent = className;
                classElement.setAttribute("onclick", "showDoc('" + className + "')");
                classNode.appendChild(classElement);
                nestedUl.appendChild(classNode);
            });

            packageNode.appendChild(nestedUl);
            ul.appendChild(packageNode);
        }
    }
}