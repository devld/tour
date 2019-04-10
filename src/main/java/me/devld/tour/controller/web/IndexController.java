package me.devld.tour.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(
                "markdown",
                "# Tour Application\n\n" +
                        "> quote test\n" +
                        "- list item 1\n" +
                        "- lite item 2\n" +
                        "- list item 3\n" +
                        "\n" +
                        "## ordered list\n\n" +
                        "1. item 1\n" +
                        "2. item 2\n" +
                        "3. item 3\n\n" +
                        "## table \n\n" +
                        "|aaa|bbb|ccc|\n" +
                        "|:--|:-:|--:|\n" +
                        "|111|222|333|\n" +
                        "|444|555|666|\n\n" +
                        "## code block\n\n" +
                        "```js\n" +
                        "const a = 123\n" +
                        "console.log(a)\n" +
                        "```\n\n"
        );
        return "index";
    }

}
