package pt.upskill.projeto2.financemanager.categories;

import java.util.ArrayList;
import java.util.List;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Category {
    private List<String> tagsList = new ArrayList<>();
    private String category;

    public Category(String string) {
        this.category = string;
    }

    //Used to read from file in the beginning and to add a new category during the runtime of the app
    public static List<Category> readCategories(String string, List<Category> categories) {
        return null;
    }

    public String getName() {
        return category;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public boolean hasDescription(String purchase) {
        if (!tagsList.isEmpty()) {
            for (String tag : tagsList) {
                if (tag.equals(purchase)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addDescription(String purchase) {
        tagsList.add(purchase);
    }
}
