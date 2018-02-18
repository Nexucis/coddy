package org.crunchytorch.coddy.snippet.elasticsearch.query.field;

import org.crunchytorch.coddy.snippet.elasticsearch.query.AbstractSnippetQueryFieldBuilder;
import org.crunchytorch.coddy.snippet.elasticsearch.query.BoolOperand;
import org.elasticsearch.index.query.QueryBuilders;

public class TitleFieldBuilder extends AbstractSnippetQueryFieldBuilder<TitleFieldBuilder> {

    private static final String FIELD = "title";

    @Override
    public TitleFieldBuilder addWord(String word) {
        this.words.add(word);
        return this;
    }

    @Override
    public TitleFieldBuilder buildQuery() {
        this.words.forEach(word -> queryBuilderList.add(QueryBuilders.regexpQuery(TitleFieldBuilder.FIELD, ".*" + word + ".*")));
        return this;
    }

    @Override
    public TitleFieldBuilder setOperand(BoolOperand operand) {
        this.operand = operand;
        return this;
    }
}
