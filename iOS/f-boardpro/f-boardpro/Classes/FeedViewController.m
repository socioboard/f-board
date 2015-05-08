//
//  FeedViewController.m
//  SwitchUserSample
//
//  Created by Socioboard 1 on 4/20/15.
//
//

#import "FeedViewController.h"
#import "UIImageView+WebCache.h"

@interface FeedViewController ()

@end

@implementation FeedViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor=[UIColor grayColor];
    [self fetchUserData];
    
    self.tableView=[[UITableView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height) style:UITableViewStylePlain];
    self.tableView.delegate=self;
    self.tableView.dataSource=self;
    self.view.backgroundColor=[UIColor grayColor];
    [self.view addSubview:self.tableView];
    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
 return    self.mutArray.count;
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    self.navigationController.navigationBarHidden=YES;

}

-(void)fetchUserData{
    self.mutArray=[[NSUserDefaults standardUserDefaults]objectForKey:@"ValueArray"];
    
    NSLog(@"mut array %@",self.mutArray);


}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
 NSDictionary *dict= [self.mutArray objectAtIndex:indexPath.row];
    NSString *picUrl=[dict valueForKey:@"picture"];
    
    
 [cell.imageView setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"58.png"]];
    cell.detailTextLabel.text=[dict valueForKey:@"description"];
    cell.textLabel.text=[dict valueForKey:@"story"];

    
    
    return cell;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
