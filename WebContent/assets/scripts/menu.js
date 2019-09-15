function activateMenu(... params) {
    params.forEach(t=>{
        if(t !==null && t!==""){
            console.log("attempting to activate menu " + t);
            const node = document.querySelector("#" + t.trim());
            if(node.children[0].childElementCount === 3){
                node.children[0].children[2].classList.toggle('open');
            }

            node.classList.add("start", "active", "open");

        }
    });
}