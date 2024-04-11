namespace Model;
using Microsoft.EntityFrameworkCore;
public class AppDbContext : DbContext
{
  //content_config
  public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
  {
  }
  protected override void OnModelCreating(ModelBuilder modelBuilder)
  {
    //Entity_frameworks_primary_key
  }
}