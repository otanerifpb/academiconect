package br.edu.ifpb.pweb2.academiConect.ui;

public class NavPageBuilder {

    private NavPage paginator;

    public static NavPage newNavPage(int currentPage, long totalItems, int totalPages, int pageSize) {
        
        NavPageBuilder builder = new NavPageBuilder();
        builder.start();
        builder.setCurrentPage(currentPage);
        builder.setTotalItems(totalItems);
        builder.setTotalPages(totalPages);
        builder.setPageSize(pageSize);
        return builder.finish();
    }

    private NavPageBuilder() {
        this.start();
    }

    public NavPageBuilder start() {
        this.paginator = new NavPage();
        return this;
    }

    public NavPageBuilder setCurrentPage(int currentPage) {
        this.paginator.setCurrentPage(currentPage);
        return this;
    }

    public NavPageBuilder setTotalItems(long totalItems) {
        this.paginator.setTotalItems(totalItems);
        return this;
    }

    public NavPageBuilder setTotalPages(int totalPages) {
        this.paginator.setTotalPages(totalPages);
        return this;
    }

    public NavPageBuilder setPageSize(int pageSize) {
        this.paginator.setPageSize(pageSize);
        return this;
    }

    public NavPage finish() {
        return this.paginator;
    }

    
}
